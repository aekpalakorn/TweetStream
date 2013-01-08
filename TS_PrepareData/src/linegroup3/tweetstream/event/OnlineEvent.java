package linegroup3.tweetstream.event;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import linegroup3.tweetstream.postprocess.ValueTermPair;

public class OnlineEvent {
	
	private Timestamp start = null;
	private Timestamp end = null;
	
	private Set<String> keywords = new TreeSet<String>();

	private Map<Timestamp, Burst> bursts = new TreeMap<Timestamp, Burst>();
	
	public OnlineEvent(Burst burst){
		this.start = burst.getTime();
		add(burst);
	}
	
	public void add(Burst burst){// must add busts in order by time
		end = burst.getTime();
		
		bursts.put(burst.getTime(), burst);
		
		Set<String> words = burst.getDistribution().keySet();
		ValueTermPair[] array = new ValueTermPair[words.size()];
		int index = 0;
		for(String word : words){
			array[index] = new ValueTermPair(support(word), word);
			index ++;
		}
		
		Arrays.sort(array, new Comparator<ValueTermPair>(){

			@Override
			public int compare(ValueTermPair o1, ValueTermPair o2) {
				if(o1.v < o2.v)	return 1;
				if(o1.v > o2.v)	return -1;
				return 0;
			}});
		
		
		int TOP_N = 3;
		for(int i = 0; i < TOP_N && i < words.size(); i ++){
			String word = array[i].term;
			if(support(word) >= BurstCompare.MIN_SUPPORT){
				keywords.add(word);
			}
		}
	}
	
	public Timestamp getStart(){
		return start;
	}
	
	public Timestamp getEnd(){
		return end;
	}
	
	public Set<String> getKeywords(){
		return keywords;
	}
	
	public Map<Timestamp, Burst> getBursts(){
		return bursts;
	}
	
	public String toString(){
		String ret = "[" + start + "," + end + "]\t";
		for(String word : keywords){
			ret += (word + ",");
		}
		return ret;
	}
	
	public double support(String word){
		double s = 0.0;
		for(Burst burst : bursts.values()){
			Double p = burst.getDistribution().get(word);
			if(p != null){
				s += p;
			}
		}	
		
		return s / bursts.size();
	}
}