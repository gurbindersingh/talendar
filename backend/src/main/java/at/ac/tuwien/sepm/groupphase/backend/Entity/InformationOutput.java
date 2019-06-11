package at.ac.tuwien.sepm.groupphase.backend.Entity;

import java.util.Arrays;
import java.util.Objects;

public class InformationOutput {
    private String filename;
    private byte[] contents;

    public InformationOutput(){}


    public InformationOutput(String filename, byte[] contents) {
        this.filename = filename;
        this.contents = contents;
    }


    public String getFilename() {
        return filename;
    }


    public void setFilename(String filename) {
        this.filename = filename;
    }


    public byte[] getContents() {
        return contents;
    }


    public void setContents(byte[] contents) {
        this.contents = contents;
    }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        InformationOutput that = (InformationOutput) o;
        return Objects.equals(filename, that.filename) &&
               Arrays.equals(contents, that.contents);
    }


    @Override
    public int hashCode() {
        int result = Objects.hash(filename);
        result = 31 * result + Arrays.hashCode(contents);
        return result;
    }


    @Override
    public String toString() {
        return "InformationOutput{" +
               "filename='" + filename + '\'' +
               ", contents=" + Arrays.toString(contents) +
               '}';
    }
}
