package com.example.smartpantrymanager.data;

import android.content.*;import android.database.sqlite.*;import android.database.Cursor;
import com.example.smartpantrymanager.model.*;import java.util.*;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB="smart_pantry.db"; private static final int VER=1;
    public DatabaseHelper(Context c){super(c,DB,null,VER);}
    @Override public void onCreate(SQLiteDatabase db){
        db.execSQL("CREATE TABLE pantry(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,quantity REAL NOT NULL,unit TEXT NOT NULL,expiry TEXT)");
        db.execSQL("CREATE TABLE recipes(id INTEGER PRIMARY KEY AUTOINCREMENT,name TEXT NOT NULL,ingredients TEXT NOT NULL,steps TEXT NOT NULL)"); seed(db);
    }
    @Override public void onUpgrade(SQLiteDatabase db,int oldV,int newV){db.execSQL("DROP TABLE IF EXISTS pantry");db.execSQL("DROP TABLE IF EXISTS recipes");onCreate(db);}
    private void seed(SQLiteDatabase db){String[][] r={
        {"Pasta Tomato","pasta, tomato, onion, garlic, olive oil","Boil pasta. Saute onion and garlic in oil. Add tomato and toss with pasta."},
        {"Cheese Omelette","egg, cheese, butter, salt","Beat eggs. Melt butter, add eggs and cheese. Fold when set."},
        {"Vegetable Fried Rice","rice, egg, carrot, onion, soy sauce","Stir-fry onion and carrot. Add rice and soy sauce, then stir in egg."},
        {"Chicken Rice Bowl","chicken, rice, onion, garlic, soy sauce","Cook chicken. Saute onion and garlic. Add rice and soy sauce."},
        {"Tuna Sandwich","bread, tuna, mayonnaise, lettuce","Mix tuna and mayonnaise. Fill bread with tuna mixture and lettuce."},
        {"Tomato Egg Toast","bread, tomato, egg, cheese","Toast bread. Cook egg and tomato, top with cheese and serve."},
        {"Garlic Butter Pasta","pasta, garlic, butter, cheese","Boil pasta. Melt butter with garlic and toss with pasta and cheese."},
        {"Chicken Pasta","pasta, chicken, tomato, garlic","Cook pasta. Cook chicken with garlic and tomato. Combine."},
        {"Bean Rice Bowl","rice, beans, tomato, onion","Warm beans. Saute onion and tomato. Serve over rice."},
        {"Potato Omelette","potato, egg, onion, oil","Cook sliced potato and onion in oil. Add beaten eggs and set."},
        {"Grilled Cheese","bread, cheese, butter","Butter bread. Add cheese and grill until golden and melted."},
        {"Chicken Sandwich","bread, chicken, lettuce, mayonnaise","Cook chicken. Spread mayonnaise on bread and add chicken and lettuce."},
        {"Tomato Salad","tomato, onion, lettuce, olive oil, salt","Chop vegetables. Toss with olive oil and salt."},
        {"Rice and Egg","rice, egg, butter, soy sauce","Cook egg in butter. Add rice and soy sauce and stir."},
        {"Garlic Chicken","chicken, garlic, butter, salt","Cook chicken in butter. Add garlic and cook until fragrant and chicken is done."},
        {"Cheesy Rice","rice, cheese, butter","Warm rice with butter and fold through cheese until melted."},
        {"Tuna Rice","rice, tuna, mayonnaise","Mix warm rice with tuna and mayonnaise."},
        {"Egg Sandwich","bread, egg, mayonnaise, lettuce","Cook egg. Mix with mayonnaise and fill bread with lettuce."},
        {"Simple Pasta Salad","pasta, tomato, onion, olive oil, salt","Cook and cool pasta. Add chopped vegetables, oil and salt."},
        {"Chicken Tomato Stew","chicken, tomato, onion, garlic, salt","Brown chicken. Add onion, garlic and tomato. Simmer until cooked."}
    }; for(String[] x:r){ContentValues v=new ContentValues();v.put("name",x[0]);v.put("ingredients",x[1]);v.put("steps",x[2]);db.insert("recipes",null,v);}}
    public List<PantryItem> getPantry(){List<PantryItem> l=new ArrayList<>();Cursor c=getReadableDatabase().rawQuery("SELECT * FROM pantry ORDER BY name",null);while(c.moveToNext())l.add(new PantryItem(c.getLong(0),c.getString(1),c.getDouble(2),c.getString(3),c.getString(4)));c.close();return l;}
    public long addPantry(String n,double q,String u,String e){ContentValues v=new ContentValues();v.put("name",n);v.put("quantity",q);v.put("unit",u);v.put("expiry",e);return getWritableDatabase().insert("pantry",null,v);}
    public int updatePantry(long id,String n,double q,String u,String e){ContentValues v=new ContentValues();v.put("name",n);v.put("quantity",q);v.put("unit",u);v.put("expiry",e);return getWritableDatabase().update("pantry",v,"id=?",new String[]{String.valueOf(id)});}
    public int deletePantry(long id){return getWritableDatabase().delete("pantry","id=?",new String[]{String.valueOf(id)});}
    public PantryItem getPantryItem(long id){Cursor c=getReadableDatabase().rawQuery("SELECT * FROM pantry WHERE id=?",new String[]{String.valueOf(id)});PantryItem p=null;if(c.moveToFirst())p=new PantryItem(c.getLong(0),c.getString(1),c.getDouble(2),c.getString(3),c.getString(4));c.close();return p;}
    public List<Recipe> getRecipes(){List<Recipe> l=new ArrayList<>();Cursor c=getReadableDatabase().rawQuery("SELECT * FROM recipes ORDER BY name",null);while(c.moveToNext())l.add(new Recipe(c.getLong(0),c.getString(1),c.getString(2),c.getString(3)));c.close();return l;}
    public Recipe getRecipe(long id){Cursor c=getReadableDatabase().rawQuery("SELECT * FROM recipes WHERE id=?",new String[]{String.valueOf(id)});Recipe r=null;if(c.moveToFirst())r=new Recipe(c.getLong(0),c.getString(1),c.getString(2),c.getString(3));c.close();return r;}
    public List<Recipe> getStrictMatches(){List<PantryItem> p=getPantry();Map<String,Double> stock=new HashMap<>();for(PantryItem x:p)stock.put(normalize(x.getName()),x.getQuantity());List<Recipe> out=new ArrayList<>();for(Recipe r:getRecipes()){boolean ok=true;for(String ing:r.ingredientList()){String key=normalize(ing);if(!stock.containsKey(key)||stock.get(key)<1){ok=false;break;}}if(ok)out.add(r);}return out;}
    private String normalize(String s){s=s.toLowerCase(Locale.US).trim();if(s.endsWith("ies"))s=s.substring(0,s.length()-3)+"y";else if(s.endsWith("es"))s=s.substring(0,s.length()-2);else if(s.endsWith("s"))s=s.substring(0,s.length()-1);return s;}
}
