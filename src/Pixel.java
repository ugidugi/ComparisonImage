public class Pixel{
	private int x;
	private int y;
	private boolean chek;
	private int claster;
	
	public Pixel(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	
	public boolean getChek(){
		return chek;
	}
	
	public int getClaster(){
		return claster;
	}
	
	public void setChek(boolean chek){
		this.chek = chek;
	}
	
	public void setClaster(int claster){
		this.claster = claster;
	}
}