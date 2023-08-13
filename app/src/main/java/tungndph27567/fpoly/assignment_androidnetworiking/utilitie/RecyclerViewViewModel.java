package tungndph27567.fpoly.assignment_androidnetworiking.utilitie;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RecyclerViewViewModel extends ViewModel {
    private MutableLiveData<Integer> recyclerViewPosition = new MutableLiveData<>();

    public MutableLiveData<Integer> getRecyclerViewPosition() {
        return recyclerViewPosition;
    }

    public void setRecyclerViewPosition(int position) {
        recyclerViewPosition.setValue(position);
    }
}
