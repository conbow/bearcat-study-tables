package edu.uc.bearcatstudytables.ui.viewmodel;

import android.databinding.ObservableBoolean;

import java.util.ArrayList;
import java.util.List;

import edu.uc.bearcatstudytables.dto.ChatDTO;
import edu.uc.bearcatstudytables.dto.UserDTO;
import edu.uc.bearcatstudytables.ui.viewmodel.common.TaskDataViewModel;

public class ChatViewModel extends TaskDataViewModel<ChatDTO> {

    private ObservableBoolean isLoadingStudents = new ObservableBoolean(false);

    private List<UserDTO> students = new ArrayList<>();

    public ObservableBoolean getIsLoadingStudents() {
        return isLoadingStudents;
    }

    public void setIsLoadingStudents(boolean isLoading) {
        this.isLoadingStudents.set(isLoading);
    }

    public List<UserDTO> getStudents() {
        return students;
    }

    public void setStudents(List<UserDTO> students) {
        this.students = students;
    }
}
