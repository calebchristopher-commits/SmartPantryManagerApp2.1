package com.example.smartpantrymanager.model;
import java.util.*;
public class Recipe {
    private long id; private String name; private String ingredients; private String steps;
    public Recipe(long id,String name,String ingredients,String steps){this.id=id;this.name=name;this.ingredients=ingredients;this.steps=steps;}
    public long getId(){return id;} public String getName(){return name;} public String getIngredients(){return ingredients;} public String getSteps(){return steps;}
    public List<String> ingredientList(){return Arrays.asList(ingredients.split("\\s*,\\s*"));}
}
