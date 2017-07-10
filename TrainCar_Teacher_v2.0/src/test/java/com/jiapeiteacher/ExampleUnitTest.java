package com.guugoo.jiapeiteacher;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testStrSplit() {
        String str = "4&尚学凯&13781936563&410782199002151111";
        String[] strArray = str.split("\\|");
        System.out.println(strArray.length);
        System.out.println(Arrays.toString(strArray));
    }
}