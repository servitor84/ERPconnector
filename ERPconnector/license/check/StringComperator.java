
package de.di.license.check;

import com.google.common.collect.ComparisonChain;
import java.util.Comparator;

/**
 *
 * @author di
 */
class StringComperator {
    
    static final Comparator<String> stringAlphabeticalComparator = new Comparator<String>() {
       
        public int compare(String str1, String str2) {
            return ComparisonChain.start().
                                compare(str1,str2, String.CASE_INSENSITIVE_ORDER).
                                compare(str1,str2).
                                result();
         }
 };
    
}
