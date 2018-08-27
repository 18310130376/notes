import com.google.common.base.Optional;

public class OptionalDemo {
    public static void main(String[] args) {
        Optional<Student> possibleNull = Optional.of(new Student());
        possibleNull.get();
    }
    public static class Student { }
}