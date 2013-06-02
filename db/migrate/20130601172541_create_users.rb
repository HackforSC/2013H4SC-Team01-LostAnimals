class CreateUsers < ActiveRecord::Migration
  def change
    create_table :users do |t|

      t.integer :facebook_id
      t.string :email
      t.string :crypted_password
      t.string :password_salt
      t.string :persistence_token

      t.float :perm_location_lat
      t.float :perm_location_long

      t.float :last_location_lat
      t.float :last_location_long

      t.integer     :failed_login_count,    :null => false, :default => 0
      t.integer     :login_count,           :null => false, :default => 0

      t.datetime    :last_request_at
      t.datetime    :current_login_at
      t.datetime    :last_login_at

      t.timestamps
    end
  end
end