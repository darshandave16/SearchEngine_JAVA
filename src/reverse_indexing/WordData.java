package reverse_indexing;


public class WordData 
{
	String restaurant;
	int frequency;
	public WordData(String restaurant, int frequency)
	{
		this.restaurant = restaurant;
		this.frequency = frequency;
	}
	
	public String getRestaurant()
	{
		return restaurant;
	} 
	public int getFrequency()
	{
		return frequency;
	} 
}