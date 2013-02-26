package test;

import java.sql.Timestamp;

import linegroup3.tweetstream.rt2.RTProcess2;

public class RTProcess2Test {

	public static void main(String[] args) throws Exception{
		RTProcess2 pc = new RTProcess2();
		Timestamp t = new Timestamp(0);
		pc.runTime(t);
	}
}
