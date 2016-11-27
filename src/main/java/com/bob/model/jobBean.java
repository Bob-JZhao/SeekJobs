package com.bob.model;

public class jobBean {
	public jobBean(int id ,String title,String location,String area , advertiser advertiser,String teaser){
		this.id = id ;
		this.title = title;
		this.location = location ;
		this .advertiser = advertiser;
		this.teaser = teaser ;
	}
	public int id;
	public String title;
	public String location;
	public String area;
	public advertiser advertiser;
	public String teaser;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public advertiser getAdvertiser() {
		return advertiser;
	}

	public void setAdvertiser(advertiser advertiser) {
		this.advertiser = advertiser;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getTeaser() {
		return teaser;
	}

	public void setTeaser(String teaser) {
		this.teaser = teaser;
	}

	@Override
	public String toString() {
		return "jobBean [id=" + id + ", title=" + title + ", location=" + location + ", area=" + area + ", advertiser="
				+ advertiser + ", teaser=" + teaser + "]";
	}

}
