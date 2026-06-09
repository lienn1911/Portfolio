package project.partb.handleData;

import java.util.List;
import java.util.stream.Collectors;

public class DataFilter {
    public List<String[]> filterDataByUser(List<String[]> data, String username) {
        return data.stream()
                .filter(record -> record[0].equals(username))
                .collect(Collectors.toList());
    }
}