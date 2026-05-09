package es.sebas1705.authentication.helpers

import android.app.Activity
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.RuntimeExecutionException
import com.google.android.gms.tasks.Task
import java.util.concurrent.Executor

/**
 * Minimal [Task] fakes for JVM unit tests.
 *
 * We only implement the methods that our production code (via TaskFlowManager) uses:
 * - addOnSuccessListener
 * - addOnFailureListener
 *
 * Everything else throws to keep the fake small and to surface unexpected calls.
 */

class ImmediateSuccessTask<T>(private val value: T?) : Task<T>() {

    override fun addOnSuccessListener(listener: OnSuccessListener<in T>): Task<T> {
        listener.onSuccess(value)
        return this
    }

    override fun addOnSuccessListener(executor: Executor, listener: OnSuccessListener<in T>): Task<T> =
        addOnSuccessListener(listener)

    override fun addOnSuccessListener(activity: Activity, listener: OnSuccessListener<in T>): Task<T> =
        addOnSuccessListener(listener)

    override fun addOnFailureListener(listener: OnFailureListener): Task<T> = this

    override fun addOnFailureListener(executor: Executor, listener: OnFailureListener): Task<T> = this

    override fun addOnFailureListener(activity: Activity, listener: OnFailureListener): Task<T> = this

    // --- Unused Task API ---
    override fun isComplete(): Boolean = true
    override fun isSuccessful(): Boolean = true
    override fun isCanceled(): Boolean = false
    override fun getResult(): T? = value

    override fun <X : Throwable?> getResult(exceptionType: Class<X>): T? = value

    override fun getException(): Exception? = null

    override fun addOnCanceledListener(listener: com.google.android.gms.tasks.OnCanceledListener): Task<T> =
        throw UnsupportedOperationException("Not needed in tests")

    override fun addOnCanceledListener(executor: Executor, listener: com.google.android.gms.tasks.OnCanceledListener): Task<T> =
        throw UnsupportedOperationException("Not needed in tests")

    override fun addOnCanceledListener(activity: Activity, listener: com.google.android.gms.tasks.OnCanceledListener): Task<T> =
        throw UnsupportedOperationException("Not needed in tests")
}

class ImmediateFailureTask<T>(private val exception: Exception) : Task<T>() {

    override fun addOnSuccessListener(listener: OnSuccessListener<in T>): Task<T> = this

    override fun addOnSuccessListener(executor: Executor, listener: OnSuccessListener<in T>): Task<T> = this

    override fun addOnSuccessListener(activity: Activity, listener: OnSuccessListener<in T>): Task<T> = this

    override fun addOnFailureListener(listener: OnFailureListener): Task<T> {
        listener.onFailure(exception)
        return this
    }

    override fun addOnFailureListener(executor: Executor, listener: OnFailureListener): Task<T> =
        addOnFailureListener(listener)

    override fun addOnFailureListener(activity: Activity, listener: OnFailureListener): Task<T> =
        addOnFailureListener(listener)

    // --- Unused Task API ---
    override fun isComplete(): Boolean = true
    override fun isSuccessful(): Boolean = false
    override fun isCanceled(): Boolean = false
    override fun getResult(): T? = throw RuntimeExecutionException(exception)

    override fun <X : Throwable?> getResult(exceptionType: Class<X>): T? = throw RuntimeExecutionException(exception)

    override fun getException(): Exception = exception

    override fun addOnCanceledListener(listener: com.google.android.gms.tasks.OnCanceledListener): Task<T> =
        throw UnsupportedOperationException("Not needed in tests")

    override fun addOnCanceledListener(executor: Executor, listener: com.google.android.gms.tasks.OnCanceledListener): Task<T> =
        throw UnsupportedOperationException("Not needed in tests")

    override fun addOnCanceledListener(activity: Activity, listener: com.google.android.gms.tasks.OnCanceledListener): Task<T> =
        throw UnsupportedOperationException("Not needed in tests")
}
