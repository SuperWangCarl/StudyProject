package com.java8;

import java.util.List;

@FunctionalInterface
public interface IMyCreate<T extends List<?>> {

	T create();
}
