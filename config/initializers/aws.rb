AWS_CONFIG = YAML.load_file("#{::Rails.root}/config/aws.yml")[::Rails.env]
