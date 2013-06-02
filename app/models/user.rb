class User < ActiveRecord::Base
  has_many :found, :class_name => "Pet",
      :finder_sql => 'SELECT p.* FROM pets p ' +
        'JOIN users u on u.id = a.finder ' +
        'WHERE u.id=#{id}'

  has_many :lost, :class_name => "Pet",
      :finder_sql => 'SELECT p.* FROM pets p ' +
        'JOIN users u on u.id = a.owner ' +
        'WHERE u.id=#{id}'

  attr_accessible :email, :password, :password_confirmation, :facebook_id, :last_location_lat, :last_location_long, :password_hash, :perm_location_lat, :perm_location_long

  acts_as_authentic
end
