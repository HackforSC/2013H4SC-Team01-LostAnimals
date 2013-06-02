require 'test_helper'

class UsersControllerTest < ActionController::TestCase
  setup do
    @user = users(:one)
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:users)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create user" do
    assert_difference('User.count') do
      post :create, user: { email: @user.email, facebook_id: @user.facebook_id, last_location_lat: @user.last_location_lat, last_location_long: @user.last_location_long, password: @user.password, perm_location_lat: @user.perm_location_lat, perm_location_long: @user.perm_location_long }
    end

    assert_redirected_to user_path(assigns(:user))
  end

  test "should show user" do
    get :show, id: @user
    assert_response :success
  end

  test "should get edit" do
    get :edit, id: @user
    assert_response :success
  end

  test "should update user" do
    put :update, id: @user, user: { email: @user.email, facebook_id: @user.facebook_id, last_location_lat: @user.last_location_lat, last_location_long: @user.last_location_long, password: @user.password, perm_location_lat: @user.perm_location_lat, perm_location_long: @user.perm_location_long }
    assert_redirected_to user_path(assigns(:user))
  end

  test "should destroy user" do
    assert_difference('User.count', -1) do
      delete :destroy, id: @user
    end

    assert_redirected_to users_path
  end
end
