package Uebung06;


public class OutputHandler {
	
	private final long starttime;
	private final int maxLevel; 

	public OutputHandler(int maxLevel) {
		this.starttime = System.currentTimeMillis();
		this.maxLevel = maxLevel;
	}
	
	public void output(int level, String output) {
		if(level <= this.maxLevel) System.out.println("-- " + output + " -- ["+ (System.currentTimeMillis()-starttime) +"] --------------");
	}
	
}