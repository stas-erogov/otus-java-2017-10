package ru.otus.qunit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.otus.l51.QUnitOperation;

import static org.junit.Assert.*;

public class QUnitOperationJUnitTest {
    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void add() throws Exception {
        assertEquals(7, QUnitOperation.add(2,5));
    }

    @Test
    public void sub() throws Exception {
        assertEquals(2, QUnitOperation.sub(7,5));
    }

    @Test
    public void setAnInt() throws Exception {
        QUnitOperation op = new QUnitOperation();
        op.setAnInt(7);
        assertTrue(op.getAnInt() == 7);
    }
}