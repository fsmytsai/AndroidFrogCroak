package ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2017/7/22.
 */

public class ImageData {
    public List<Prediction> Predictions = new ArrayList<>();

    public static class Prediction {
        public String TagId;
        public String Tag;
        public Double Probability;
    }
}
