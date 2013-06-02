class CreatePets < ActiveRecord::Migration
  def change
    create_table :pets do |t|
      t.string :name

      t.string :picture

      t.string :animal_type
      t.string :breed
      t.string :color
      t.string :size
      t.string :sex

      t.text :description

      t.string :status, :default => 'Lost'

      t.references :owner
      t.references :finder

      t.float :last_seen_location_lat
      t.float :last_seen_location_long

      t.float :cur_location_lat
      t.float :cur_location_long

      t.timestamps
    end
    add_index :pets, :owner_id
    add_index :pets, :finder_id
  end
end
