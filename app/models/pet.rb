class Pet < ActiveRecord::Base
  belongs_to :owner, :class_name => "User"
  belongs_to :finder, :class_name => "User"

  attr_accessible :cur_location_lat, :cur_location_long, :description, :last_seen_location_lat, :last_seen_location_long,
            :name, :picture, :status, :animal_type, :breed, :sex, :color, :size

  def self.localPets(range = 25, location_lat, location_long)
    begin
      range = range.to_f * 1609
    rescue
      range = 25*1609
    end

    self.find_by_sql(
      "SELECT * FROM pets WHERE " +
      "earth_box(" +
        "ll_to_earth(#{location_lat}, #{location_long}), " +
        "#{range}" +
      ") @> ll_to_earth(last_seen_location_lat, last_seen_location_long)" +
      " AND updated_at > current_timestamp - interval '21' day" +
      " ORDER BY updated_at DESC"
    )
  end

  def self.uploadPicture(uploaded_io)
    begin
      require 'aws/s3'
      AWS::S3::Base.establish_connection!(
        :access_key_id     => AWS_CONFIG['access_key'],
        :secret_access_key => AWS_CONFIG['secret']
      )

      key = SecureRandom.hex() + ".jpg"
      AWS::S3::S3Object.store(key, uploaded_io, 'lostanimals', :content_type => uploaded_io.content_type, :access => :public_read)

      "https://s3.amazonaws.com/lostanimals/" + key
    rescue
      nil
    end
  end
end
