package br.com.felipeacerbi.biy.models;

import android.os.Bundle;

import org.parceler.Parcel;
import org.parceler.Parcels;

import icepick.Bundler;

@Parcel
public class Step implements Bundler<Step> {

	private int id;
	private String description;
	private String shortDescription;
	private String thumbnailURL;
	private String videoURL;

	public void setVideoURL(String videoURL){
		this.videoURL = videoURL;
	}

	public String getVideoURL(){
		return videoURL;
	}

	public void setDescription(String description){
		this.description = description;
	}

	public String getDescription(){
		return description;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id;
	}

	public void setShortDescription(String shortDescription){
		this.shortDescription = shortDescription;
	}

	public String getShortDescription(){
		return shortDescription;
	}

	public void setThumbnailURL(String thumbnailURL){
		this.thumbnailURL = thumbnailURL;
	}

	public String getThumbnailURL(){
		return thumbnailURL;
	}

	@Override
 	public String toString(){
		return 
			"Step{" +
			"videoURL = '" + videoURL + '\'' + 
			",description = '" + description + '\'' + 
			",id = '" + id + '\'' + 
			",shortDescription = '" + shortDescription + '\'' + 
			",thumbnailURL = '" + thumbnailURL + '\'' + 
			"}";
		}

    @Override
    public void put(String key, Step step, Bundle bundle) {
        bundle.putParcelable(key, Parcels.wrap(step));
    }

    @Override
    public Step get(String key, Bundle bundle) {
        return Parcels.unwrap(bundle.getParcelable(key));
    }
}
