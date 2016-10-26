package by.bsu.cinemarating.entity;

public class Movie extends Entity {
    private String name;
    private String description;
    private int year;
    private String country;
    private double rating;
    private String ref;
    private int authorId;

    public Movie(int id, String name, String description, int year, String country, double rating, String ref) {
        super(id);
        this.name = name;
        this.description = description;
        this.year = year;
        this.country = country;
        this.rating = rating;
        this.ref = ref;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (year != movie.year) return false;
        if (Double.compare(movie.rating, rating) != 0) return false;
        if (!name.equals(movie.name)) return false;
        if (!description.equals(movie.description)) return false;
        if (!country.equals(movie.country)) return false;
        return ref != null ? ref.equals(movie.ref) : movie.ref == null;

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + year;
        result = 31 * result + country.hashCode();
        temp = Double.doubleToLongBits(rating);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (ref != null ? ref.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id='" + getId() + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", year=" + year +
                ", country='" + country + '\'' +
                ", rating=" + rating +
                ", ref='" + ref + '\'' +
                '}';
    }
}
