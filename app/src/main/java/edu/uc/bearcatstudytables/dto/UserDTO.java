package edu.uc.bearcatstudytables.dto;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import edu.uc.bearcatstudytables.BR;

@IgnoreExtraProperties
public class UserDTO extends BaseObservable {

    public enum Type {STUDENT, INSTRUCTOR}

    private String id = "";
    private Type type;
    private String email = "";
    private String name = "";
    private String password = "";
    private String photoUrl = "";
    private byte[] photo;

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
        notifyPropertyChanged(BR.type);
    }

    @Bindable
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        notifyPropertyChanged(BR.email);
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    @Exclude
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    @Bindable
    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
        notifyPropertyChanged(BR.photoUrl);
    }

    @Bindable
    @Exclude
    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
        notifyPropertyChanged(BR.photo);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof UserDTO) {
            UserDTO user = (UserDTO) obj;
            return (id.equals(user.getId())
                    && (email.equals(user.getEmail()))
                    && (name.equals(user.getName()))
                    && (photoUrl.equals(user.getPhotoUrl())));
        }
        return super.equals(obj);
    }
}
