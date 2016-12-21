package com.gntv.tv.common.utils;

import java.util.Collection;

public class CollectionUtil {

	public static <T> boolean isEmpty(Collection<T> data) {
		return (data == null) || (data.size() == 0);
	}

}
