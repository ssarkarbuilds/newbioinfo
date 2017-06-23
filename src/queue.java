import java.util.ArrayList;

public class queue {

	
	static ArrayList<jobPOJO> al=new ArrayList<>();
	
	static void push(int jid,String DATE){
		
		al.add(new jobPOJO(jid,DATE));
		System.out.println("show size of queue= "+al.size());
	/*	for(jobPOJO k:al){
			System.out.println("CONTENTS : "+k.DATE+" "+k.jid);
		}  */
		
	}
	
static String pop(){
		
		
	//	System.out.println("show sizze of queue= "+al.size());
		
		jobPOJO k=al.remove(0);
	//	System.out.println("bingo2 "+k.DATE+" "+k.jid);
		return k.jid+"_"+k.DATE;
		
		}
	
	

}
