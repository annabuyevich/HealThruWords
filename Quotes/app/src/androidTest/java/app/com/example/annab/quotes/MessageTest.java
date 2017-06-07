package app.com.example.annab.quotes;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * MessageTest:
 * Test the firebase realtime databases that the messages are connected to.
 *
 * @author anna
 */
@RunWith(AndroidJUnit4.class)

public class MessageTest {

    // client side interface of the database
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    private DatabaseReference messageReferenceCount =
            firebaseDatabase.getReference("messages");
    private DatabaseReference messageReference =
            firebaseDatabase.getReference("/messages/-KiXKTmvILkETg0ChPPP/text");
    private DatabaseReference messageReferenceDelete =
            firebaseDatabase.getReference("messages/-KiqPLy7FoQyRpxxnoEF");
    private int numOfChildren;


    @Test
    public void countChildren() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        numOfChildren = 0;
        messageReferenceCount.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("CHILD", dataSnapshot.toString());
                numOfChildren++;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        countDownLatch.await(10, TimeUnit.SECONDS);
        assertEquals(44, numOfChildren);
    }

    @Test
    public void delete() throws Exception {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        messageReferenceDelete.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                countDownLatch.countDown();
            }
        });

        countDownLatch.await(5, TimeUnit.SECONDS);
        assertEquals(0, countDownLatch.getCount());
    }

    @Test
    public void writeTest() throws InterruptedException {
        final CountDownLatch writeSignal = new CountDownLatch(1);

        // set value can be anything
        messageReference.setValue("none").addOnCompleteListener(new OnCompleteListener<Void>() {
            // when write actually happen call me and tell me the job is done
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                writeSignal.countDown();
            }
        });

        // wait until onComplete has been called
        writeSignal.await(5, TimeUnit.SECONDS);
        assertEquals(0, writeSignal.getCount());
    }

    @Test
    public void readTest() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        // what is the current value
        messageReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // is the value actually the one we want
                assertEquals("none",
                        dataSnapshot.getValue(String.class));
                countDownLatch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        countDownLatch.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void updateTest() throws InterruptedException {
        final CountDownLatch countDownLatch = new CountDownLatch(1);

        // set value can be anything
        messageReference.setValue("testing").addOnCompleteListener(new OnCompleteListener<Void>() {
            // when write actually happen call me and tell me the job is done
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                countDownLatch.countDown();
            }
        });

        messageReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // is the value actually the one we want
                assertEquals("testing",
                        dataSnapshot.getValue(String.class));
                countDownLatch.countDown();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // wait until onComplete has been called
        countDownLatch.await(5, TimeUnit.SECONDS);
    }

    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("app.com.example.annab.quotes", appContext.getPackageName());
    }
}

