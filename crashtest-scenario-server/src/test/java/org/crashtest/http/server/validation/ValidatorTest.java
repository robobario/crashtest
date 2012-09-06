package org.crashtest.http.server.validation;

import org.crashtest.http.server.request.Request;
import org.junit.Test;
import org.mockito.Mockito;

public class ValidatorTest {
    @Test
    public void testValidatingAValidRequest() throws ValidationException {
        Validator validator = Validator.instance();
        Request mock = Mockito.mock(Request.class);
        Mockito.when(mock.isValid()).thenReturn(true);
        validator.validate(mock);
        //expect no exceptions
    }

    @Test(expected = ValidationException.class)
    public void testValidatingAnInvalidRequest() throws ValidationException {
        Validator validator = Validator.instance();
        Request mock = Mockito.mock(Request.class);
        Mockito.when(mock.isValid()).thenReturn(false);
        validator.validate(mock);
    }
}
