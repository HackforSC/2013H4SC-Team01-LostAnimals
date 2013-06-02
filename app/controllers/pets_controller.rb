class PetsController < ApplicationController
  # GET /pets
  # GET /pets.json
  def index
    params[:range] ||= session[:range] ? session[:range] : 25
    session[:range] = params[:range]

    params[:lat] ||= session[:location] ? session[:location][:lat] : request.location.latitude
    params[:long] ||= session[:location] ? session[:location][:long] : request.location.longitude

    if params[:location]
      @search_location = Geocoder.search(params[:location])
      params[:lat] = @search_location.first.latitude
      params[:long] = @search_location.first.longitude
    else
      @search_location = Geocoder.search(params[:lat].to_s + "," + params[:long].to_s)
    end

    session[:location] = {
      :lat => params[:lat],
      :long => params[:long]
    }

    @pets = Pet.localPets(params[:range], params[:lat].to_f, params[:long].to_f)

    respond_to do |format|
      format.html # index.html.erb
      format.json { render json: @pets }
    end
  end

  # GET /pets/1
  # GET /pets/1.json
  def show
    @pet = Pet.find(params[:id])

    respond_to do |format|
      format.html # show.html.erb
      format.json { render json: @pet }
    end
  end

  # GET /pets/new
  # GET /pets/new.json
  def new
    if !current_user
      redirect_to :login, :notice => "You have to either login or register in order to report a lost/found dog."
    else
      @pet = Pet.new

      respond_to do |format|
        format.html # new.html.erb
        format.json { render json: @pet }
      end
    end
  end

  # POST /pets
  # POST /pets.json
  def create
    uploaded_io = params[:pet][:picture]

    params[:pet][:picture] = Pet.uploadPicture(uploaded_io)

    @pet = Pet.new(params[:pet])

    respond_to do |format|
      if @pet.save
        format.html { redirect_to @pet, notice: 'Pet was successfully created.' }
        format.json { render json: @pet, status: :created, location: @pet }
      else
        format.html { render action: "new" }
        format.json { render json: {:errors => @pet.errors}, status: :unprocessable_entity }
      end
    end
  end

  # PUT /pets/1
  # PUT /pets/1.json
  def update
    @pet = Pet.find(params[:id])

    respond_to do |format|
      if @pet.update_attributes(params[:pet])
        format.html { redirect_to @pet, notice: 'Pet was successfully updated.' }
        format.json { head :no_content }
      else
        format.html { render action: "edit" }
        format.json { render json: @pet.errors, status: :unprocessable_entity }
      end
    end
  end

  # DELETE /pets/1
  # DELETE /pets/1.json
  def destroy
    @pet = Pet.find(params[:id])
    @pet.destroy

    respond_to do |format|
      format.html { redirect_to pets_url }
      format.json { head :no_content }
    end
  end
end
