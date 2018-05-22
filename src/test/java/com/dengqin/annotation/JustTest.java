package com.dengqin.annotation;

import org.junit.Test;
import org.mockito.Mockito;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by dq on 2018/5/15.
 */
public class JustTest {
	public static void main(String[] args) {
		List mockedList = mock(List.class);

		// using mock object - it does not throw any "unexpected interaction"
		// exception
		mockedList.add("one");
		System.out.println("aa = " + mockedList);
		mockedList.clear();
		System.out.println("bb = " + mockedList);

		// selective, explicit, highly readable verification
		verify(mockedList).add("one");
		System.out.println("cc = " + mockedList);
		verify(mockedList).clear();
		System.out.println("dd = " + mockedList);
	}

	@Test
	public void testForMe() {
		List mockedList = mock(List.class);

		// using mock object - it does not throw any "unexpected interaction"
		// exception
		mockedList.add("one");
		// Mockito.when(mockedList.get(0)).thenReturn("127.0.0.2");
		System.out.println(mockedList.get(0));
		System.out.println("aa = " + mockedList);
		mockedList.clear();
		System.out.println("bb = " + mockedList);

		// selective, explicit, highly readable verification
		verify(mockedList).add("teo");
		System.out.println("cc = " + mockedList);
		verify(mockedList).clear();
		System.out.println("dd = " + mockedList);

	}

	@Test
	public void testHhh() {
		LinkedList mockedList = mock(LinkedList.class);

		// stubbing appears before the actual execution
		when(mockedList.get(0)).thenReturn("first");

		// the following prints "first"
		System.out.println(mockedList.get(0));

		// the following prints "null" because get(999) was not stubbed
		System.out.println(mockedList.get(1));
	}

}
