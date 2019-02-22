/**
 * 
 */
/**
 * {@link LooseSingleton} is a lazy container whose value is available to the
 * garage collector for active collection. Either relying on
 * {@link java.lang.ref.WeakReference} or {@link java.lang.ref.SoftReference}
 * via {@link WeakSingleton} and {@link SoftSingleton} respectfully.
 * 
 * <p>
 * Thread-safety is also guaranteed via minimal calls to
 * {@link java.util.concurrent.atomic.AtomicReference}
 * </p>
 * 
 * @author Isaac G.
 *
 */
package org.iag.utility.singleton;