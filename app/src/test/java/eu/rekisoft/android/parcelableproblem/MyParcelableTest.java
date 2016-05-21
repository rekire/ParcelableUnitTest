package eu.rekisoft.android.parcelableproblem;

import android.os.Parcel;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.powermock.api.mockito.PowerMockito.*;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.UUID;

import de.mobilej.ABridge;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;

/**
 * Created by rekire on 20.05.2016.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Parcel.class, ABridge.class})
public class MyParcelableTest {

    @Before
    public void setup() {
        mockStatic(ABridge.class);
        when(ABridge.callBoolean(
                eq("android.os.Parcel.nativeReadFloat(long)"),
                isNull(), any(Object[].class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                return 0f;
            }
        });
    }

    @Test
    public void demoCrash() {
        UUID id = UUID.randomUUID();
        MyParcelable data = new MyParcelable("demo", 42, id, 0f);

        Parcel parcel = Parcel.obtain();
        data.writeToParcel(parcel, 0);

        // After you're done with writing, you need to reset the parcel for reading:
        parcel.setDataPosition(0);

        // Reconstruct object from parcel and asserts:
        MyParcelable sut = MyParcelable.CREATOR.createFromParcel(parcel);
        assertEquals("demo", sut.text);
        assertEquals(42, sut.count);
        assertEquals(id, sut.id);
        assertEquals(0f, sut.problem, 0.01f);
    }
}