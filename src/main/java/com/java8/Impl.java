package com.java8;

public class Impl implements InterA,InterB,Cloneable {

	

	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public void hello() {
		InterA.super.hello();
	}

	
}
