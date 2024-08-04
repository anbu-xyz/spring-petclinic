package uk.anbu.spring.sample.petclinic

class CompareUtil {
    static boolean compareObjects(Object obj1, Object obj2, List<String> excludeFields = []) {
        if (obj1.class != obj2.class) {
            return false
        }

        def fields = obj1.properties.keySet().findAll{ !excludeFields.contains(it)}

        fields.each { field ->
            if (obj1[field] != obj2[field]) {
                return false
            }
        }

        return true
    }
}
