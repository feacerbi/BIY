package br.com.felipeacerbi.biy.models;

import android.support.annotation.NonNull;

import org.parceler.Parcel;

@Parcel
public class Ingredient implements Comparable<Ingredient> {

	private String ingredient;
	private float quantity;
	private String measure;

	@SuppressWarnings("UnusedDeclaration")
	public void setQuantity(float quantity){
		this.quantity = quantity;
	}

	public float getQuantity(){
		return quantity;
	}

	@SuppressWarnings("UnusedDeclaration")
	public void setMeasure(String measure){
		this.measure = measure;
	}

	public String getMeasure(){
		return measure;
	}

	@SuppressWarnings("UnusedDeclaration")
	public void setIngredient(String ingredient){
		this.ingredient = ingredient;
	}

	public String getIngredient(){
		return ingredient;
	}

	@Override
 	public String toString(){
		return 
			"Ingredient{" +
			"quantity = '" + quantity + '\'' + 
			",measure = '" + measure + '\'' + 
			",ingredient = '" + ingredient + '\'' + 
			"}";
		}

	@Override
	public int compareTo(@NonNull Ingredient ingredient) {
		return getIngredient().toLowerCase().trim().compareTo(ingredient.getIngredient().toLowerCase().trim());
	}
}
