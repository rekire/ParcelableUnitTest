package eu.rekisoft.android.parcelableproblem;

import android.os.Parcel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.internal.matchers.CapturingMatcher;
import org.mockito.internal.progress.HandyReturnValues;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.powermock.api.mockito.PowerMockito.doNothing;
import static org.powermock.api.mockito.PowerMockito.when;

/**
 * Created by rekire on 20.05.2016.
 */
@RunWith(PowerMockRunner.class)
public class MyParcelableTest {

    @Test
    public void parcelableVerification() {
        UUID id = UUID.randomUUID();
        MyParcelable data = new MyParcelable("demo", 42, id, 1f);

        Parcel parcel = ParcelHelper.mockParcel();
        data.writeToParcel(parcel, 0);
        //parcel.setDataPosition(0); // TODO mock this to reset the pos

        MyParcelable sut = MyParcelable.CREATOR.createFromParcel(parcel);
        assertEquals("demo", sut.text);
        assertEquals(42, sut.count);
        assertEquals(id, sut.id);
        assertEquals(1f, sut.problem, 0.01f);
    }

    @Test
    public void coverageExtras() {
        MyParcelable data = new MyParcelable("demo", 42, UUID.randomUUID(), 1f);
        data.describeContents();
        MyParcelable[] sut = MyParcelable.CREATOR.newArray(2);
        assertNotNull(sut);
        assertEquals(2, sut.length);
    }

    public static class ParcelHelper implements Answer<Object> {
        private final HandyReturnValues handyReturnValues = new HandyReturnValues();
        private final CapturingMatcher<Object> capturingMatcher = new CapturingMatcher<>();
        private int pos = 0;

        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable {
            List<Object> stack = capturingMatcher.getAllValues();
            Object value = stack.get(pos);
            pos++;
            return value;
        }

        public Integer captureInt() {
            Mockito.argThat(capturingMatcher);
            return handyReturnValues.returnFor(Integer.class);
        }

        public Float captureFloat() {
            Mockito.argThat(capturingMatcher);
            return handyReturnValues.returnFor(Float.class);
        }

        public String captureString() {
            Mockito.argThat(capturingMatcher);
            return handyReturnValues.returnFor(String.class);
        }

        public <T> T capture(Class<T> clazz) {
            Mockito.argThat(capturingMatcher);
            return handyReturnValues.returnFor(clazz);
        }

        public static Parcel mockParcel() {
            Parcel parcel = Mockito.mock(Parcel.class);
            ParcelHelper helper = new ParcelHelper();

            // TODO there are much more calls which should been mocked

            doNothing().when(parcel).writeString(helper.captureString());
            doNothing().when(parcel).writeInt(helper.captureInt());
            doNothing().when(parcel).writeSerializable(helper.capture(Serializable.class));
            doNothing().when(parcel).writeFloat(helper.captureFloat());

            when(parcel.readString()).then(helper);
            when(parcel.readInt()).then(helper);
            when(parcel.readSerializable()).then(helper);
            when(parcel.readFloat()).then(helper);

            return parcel;
        }
    }
}