# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended to check this file into your version control system.

ActiveRecord::Schema.define(:version => 20130601172541) do

  create_table "pets", :force => true do |t|
    t.string   "name"
    t.string   "picture"
    t.string   "animal_type"
    t.string   "breed"
    t.string   "color"
    t.string   "size"
    t.string   "sex"
    t.text     "description"
    t.string   "status",                  :default => "Lost"
    t.integer  "owner_id"
    t.integer  "finder_id"
    t.float    "last_seen_location_lat"
    t.float    "last_seen_location_long"
    t.float    "cur_location_lat"
    t.float    "cur_location_long"
    t.datetime "created_at",              :null => false
    t.datetime "updated_at",              :null => false
  end

  add_index "pets", ["finder_id"], :name => "index_pets_on_finder_id"
  add_index "pets", ["owner_id"], :name => "index_pets_on_owner_id"

  create_table "users", :force => true do |t|
    t.integer  "facebook_id"
    t.string   "email"
    t.string   "crypted_password"
    t.string   "password_salt"
    t.string   "persistence_token"
    t.float    "perm_location_lat"
    t.float    "perm_location_long"
    t.float    "last_location_lat"
    t.float    "last_location_long"
    t.datetime "created_at",         :null => false
    t.datetime "updated_at",         :null => false
  end

end
