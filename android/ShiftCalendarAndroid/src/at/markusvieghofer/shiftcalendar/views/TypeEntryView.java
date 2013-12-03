package at.markusvieghofer.shiftcalendar.views;

import java.util.UUID;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import at.markusvieghofer.shiftcalendar.R;
import at.markusvieghofer.shiftcalendar.adapter.api.TypeEditListener;
import at.markusvieghofer.shiftcalendar.fragments.api.TypeListener;
import at.markusvieghofer.shiftcalendar.models.Type;

public class TypeEntryView extends LinearLayout {

    public final UUID ID = UUID.randomUUID();
    private CheckedTextView cbType;
    private View contType;
    private Type type;
    private TypeListener typeListener;
    private TypeEditListener typeEditListener;

    public TypeEntryView(Context context, Type type) {
        super(context);
        this.type = type;
        initView(context);
    }

    public Type getType() {
        return type;
    }

    public void hideEditTypeOptions() {
        hide();
    }

    public void setChecked(boolean checked) {
        cbType.setChecked(checked);
    }

    public void setTypeEditListener(TypeEditListener typeEditListener) {
        this.typeEditListener = typeEditListener;
    }

    public void setTypeListener(TypeListener typeListener) {
        this.typeListener = typeListener;
    }

    protected void hide() {
        ObjectAnimator animator = ObjectAnimator.ofFloat(contType, View.ALPHA,
                0);
        animator.setDuration(500);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                contType.setVisibility(View.GONE);
            }
        });
        animator.start();
    }

    protected void show() {
        contType.setAlpha(0);
        contType.setVisibility(View.VISIBLE);
        ObjectAnimator animator = ObjectAnimator.ofFloat(contType, View.ALPHA,
                1);
        animator.setDuration(500);
        animator.start();
    }

    private void initView(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(R.layout.view_type_entry, this);
        cbType = (CheckedTextView) findViewById(R.id.rbType);
        cbType.setText(type.toString());

        contType = findViewById(R.id.contType);
        cbType.setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (contType.getVisibility() == View.GONE) {
                    typeEditListener.hideEditTypeOptions(ID);
                    show();
                } else {
                    hide();
                }
                return true;
            }
        });

        cbType.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cbType.isChecked()) {
                    typeListener.setType(null);
                } else {
                    typeListener.setType(type);
                }
                typeEditListener.hideEditTypeOptions(ID);
                cbType.toggle();
            }
        });
    }
}
