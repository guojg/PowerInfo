package com.github.common.util;

public class NewSnUtil {

		public static String getID() {
			return SequencePool.getInstance().takeSeq();
		}
	
}
