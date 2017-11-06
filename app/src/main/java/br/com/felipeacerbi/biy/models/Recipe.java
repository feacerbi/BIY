package br.com.felipeacerbi.biy.models;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.Pair;

import org.parceler.Parcel;

import java.util.List;

import br.com.felipeacerbi.biy.R;

@Parcel
public class Recipe implements Comparable<Recipe> {

	private int id;
	private String name;
	private String image;
	private int servings;
	private List<Ingredient> ingredients;
	private List<Step> steps;

	@SuppressWarnings("UnusedDeclaration")
	public void setImage(String image){
		this.image = image;
	}

	public String getImage(){
		return image;
	}

	public void setServings(int servings){
		this.servings = servings;
	}

	public int getServings(){
		return servings;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setIngredients(List<Ingredient> ingredients){
		this.ingredients = ingredients;
	}

	public List<Ingredient> getIngredients(){
		return ingredients;
	}

	public void setId(int id){
		this.id = id;
	}

	public int getId(){
		return id - 1;
	}

	@SuppressWarnings("UnusedDeclaration")
	public void setSteps(List<Step> steps){
		this.steps = steps;
	}

	public List<Step> getSteps(){
		return steps;
	}

	@Override
	public String toString(){
		return
				"Recipe{" +
						"image = '" + image + '\'' +
						",servings = '" + servings + '\'' +
						",name = '" + name + '\'' +
						",ingredients = '" + ingredients + '\'' +
						",id = '" + id + '\'' +
						",steps = '" + steps + '\'' +
						"}";
	}

	public static Pair<String, Integer> getRecipeDifficulty(Context context, Recipe recipe) {
		int stepsCount = recipe.getSteps().size();

		if (stepsCount <= context.getResources().getInteger(R.integer.easy_recipe_steps_count)) {
			return new Pair<>(context.getString(R.string.easy_recipe), context.getResources().getColor(R.color.easy_green, context.getTheme()));
		} else if (stepsCount <= context.getResources().getInteger(R.integer.medium_recipe_steps_count)) {
			return new Pair<>(context.getString(R.string.medium_recipe), context.getResources().getColor(R.color.medium_yellow, context.getTheme()));
		} else {
			return new Pair<>(context.getString(R.string.hard_recipe), context.getResources().getColor(R.color.hard_red, context.getTheme()));
		}
	}

	@Override
	public int compareTo(@NonNull Recipe recipe) {
		return getName().toLowerCase().trim().compareTo(recipe.getName().toLowerCase().trim());
	}
}