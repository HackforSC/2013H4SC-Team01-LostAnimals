# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)

Pet.create([
	{
		animal_type: 'Dog',
		breed: 'Rottweiler',
		sex: 'Male',
		color: 'Black/Brown',
		size: 'Large',

		picture: 'https://s3.amazonaws.com/lostanimals/dog_1.jpeg',

		status: 'Lost',

		description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Donec vel nunc mauris, nec dignissim diam. Quisque vitae eros sem. Aliquam magna erat, faucibus quis laoreet ac, varius at turpis. Proin gravida metus eros. Curabitur rutrum gravida fermentum. Phasellus a molestie quam. Nam adipiscing, arcu vel gravida vulputate, lacus augue gravida justo, quis vehicula purus mauris venenatis erat. Suspendisse posuere lobortis tempor. Proin dignissim, orci in consequat condimentum, purus nibh vestibulum nulla, vel convallis velit leo vitae odio. Nullam sit amet nisi nisl. Phasellus sit amet elit ligula. Donec fringilla eleifend suscipit. Proin elementum quam nunc. Vivamus nec quam nisl, feugiat convallis risus. Nunc pulvinar elit vitae quam elementum tincidunt. Aenean posuere dictum quam, sed varius sem blandit vitae',

		last_seen_location_lat: 34.1496,
		last_seen_location_long: -80.9757
	}
])