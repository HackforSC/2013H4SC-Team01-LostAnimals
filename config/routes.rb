Lostanimal::Application.routes.draw do
  resources :users

  match 'login' => 'users#login', :as => :login, :via => :get
  match 'login' => 'users#login_create', :as => :login_create, :via => :post
  match 'logout' => 'users#logout', :as => :logout, :via => :get
  match 'register' => 'users#new', :as => :register, :via => :get
  match 'register' => 'users#create', :as => :register_create, :via => :post

  match 'report' => 'pets#new', :as => :report, :via => :get

  resources :pets, :except => [:edit, :destroy] do
    collection do
      get 'local_pets'
    end
  end

  root :to => 'pets#index'
end
