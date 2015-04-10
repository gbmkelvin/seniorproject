package cn.project.camt.utils;

public class URLUtil {
	public final static class Network {
		// Baisc Url  
		public static final String BASIC_STRING  ="http://198.168.191.1";
		public static final String BASIC_URL = BASIC_STRING+"/cfc-kt";
		public static final String BASIC_URL_MOBLE = BASIC_STRING+"/";
		// Img Url
		public static final String RECIPEDETAILIMG_URL = BASIC_URL
				+ "/uploads/recipe_detail/img_file/";
		public static final String INGREDIENTIMG_URL = BASIC_URL
				+ "/uploads/ingredient_detail/img_file/";
		public static final String COOKINGMETHODIMG_URL = BASIC_URL
				+ "/uploads/cooking_method/img_file/";

		public static final String RIMG = BASIC_URL
				+ "/uploads/recipe_detail/img_file/Spicy Fish Stew.jpg";

		// Json URL
		public static final String RECIPEDETAILJSON_URL = BASIC_URL
				+ "/recipeDetails/json";
		public static final String INGREDIENTJSON_URL = BASIC_URL
				+ "/ingredientDetails/json";
		public static final String COOKINGMETHODJSON_URL = BASIC_URL
				+ "/cookingMethods/json";
		public static final String RECIPEINGREDINETJSON_URL = BASIC_URL_MOBLE
				+ "Mobile/json/jsonforgetingredient.php";

		// search URl
		public static final String SEARCH_IMG_URL = BASIC_URL
				+ "/uploads/recipe_detail/img_file/";
		public static final String SEARCH_PATH_URL = BASIC_URL
				+ "/RecipeDetails/search";

		// des url
		public static final String DESCRIPTE_URL = BASIC_URL
				+ "/RecipeDetails/descripte?id=";

	}
}
