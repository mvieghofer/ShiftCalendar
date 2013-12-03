package at.markusvieghofer.shiftcalendar.views;

import java.util.UUID;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.LinearLayout;
import at.markusvieghofer.shiftcalendar.R;
import at.markusvieghofer.shiftcalendar.adapter.api.TypeEditListener;
import at.markusvieghofer.shiftcalendar.fragments.AddTypeFragment;
import at.markusvieghofer.shiftcalendar.fragments.RemoveTypeFragment;
import at.markusvieghofer.shiftcalendar.fragments.api.TypeListener;
import at.markusvieghofer.shiftcalendar.models.Type;

public class TypeEntryView extends LinearLayout {

    protected static final String TAG = TypeEntryView.class.getName() + "TAG";
    public final UUID ID = UUID.randomUUID();
    private CheckedTextView cbType;
    private View contType;
    private Type type;
    private TypeListener typeListener;
    private TypeEditListener typeEditListener;
    private FragmentManager fragmentManager;

    public TypeEntryView(Context context, Type type) {
        super(context);
        this.type = type;
        initView(context);
    }

    public FragmentManager getFragmentManager() {
        return fragmentManager;
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

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
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

    protected void openDeleteTypeDialog(Type type) {
        RemoveTypeFragment removeTypeFrag = new RemoveTypeFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(Type.KEY, type);
        bundle.putSerializable(typeListener.getKey(), typeListener);
        removeTypeFrag.setArguments(bundle);
        removeTypeFrag.show(getFragmentManager(), RemoveTypeFragment.TAG);
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

        Button btnDeleteType = (Button) findViewById(R.id.btnDeleteType);
        btnDeleteType.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                openDeleteTypeDialog(type);
            }
        });
        Button btnEditType = (Button) findViewById(R.id.btnEditType);
        btnEditType.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                AddTypeFragment frag = new AddTypeFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable(typeListener.getKey(), typeListener);
                bundle.putSerializable(Type.KEY, type);
                frag.setArguments(bundle);
                frag.setCancelable(true);
                frag.show(fragmentManager, TAG);
            }
        });
    }
}
