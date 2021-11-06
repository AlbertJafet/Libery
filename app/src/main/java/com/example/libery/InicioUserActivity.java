package com.example.libery;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.LinearLayout;

import com.example.libery.Fragment.LibroUserFragment;
import com.example.libery.Guide.GuiaUserActivity;
import com.example.libery.databinding.ActivityInicioUserBinding;
import com.example.libery.models.ModeloCat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class InicioUserActivity extends AppCompatActivity {
    private ActivityInicioUserBinding binding;
    private FirebaseAuth firebaseAuth;
    public ArrayList<ModeloCat> catArrayList;
    public ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInicioUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        ValidacionUsuario();

        setupViewPagerAdapter(binding.viewPage);
        binding.tabsLayou.setupWithViewPager(binding.viewPage);

        binding.OffLineBtn.setOnClickListener(new View.OnClickListener() {//Logout
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                ValidacionUsuario();
            }
        });

        binding.GuiaBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(InicioUserActivity.this, GuiaUserActivity.class));
            }
        });
    }

    private void setupViewPagerAdapter(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this);
        catArrayList = new ArrayList<>();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Categorias Libros");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                catArrayList.clear();

                //datos a los modelos
                ModeloCat modeloAll = new ModeloCat("01", "Todos", "", "1");
                ModeloCat modeloDownloader = new ModeloCat("02", "Descargados", "", "1");

                catArrayList.add(modeloAll);
                catArrayList.add(modeloDownloader);

                viewPagerAdapter.addFragmemt(LibroUserFragment.newInstance(//todos
                        "" + modeloAll.getId(),
                        "" + modeloAll.getCategoria(),
                        "" + modeloAll.getUid()),
                        modeloAll.getCategoria());
                viewPagerAdapter.addFragmemt(LibroUserFragment.newInstance(//descargados
                        "" + modeloDownloader.getId(),
                        "" + modeloDownloader.getCategoria(),
                        "" + modeloDownloader.getUid()),
                        modeloDownloader.getCategoria());

                viewPagerAdapter.notifyDataSetChanged();//refrsh

                for (DataSnapshot ds : snapshot.getChildren()) {//cargamos de firebase
                    ModeloCat modeloCat = ds.getValue(ModeloCat.class);

                    catArrayList.add(modeloCat);

                    viewPagerAdapter.addFragmemt(LibroUserFragment.newInstance(
                            "" + modeloCat.getId(),
                            "" + modeloCat.getCategoria(),
                            "" + modeloCat.getUid())
                            , modeloCat.getCategoria());

                    viewPagerAdapter.notifyDataSetChanged();
                }

                viewPager.setAdapter(viewPagerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {//Fragment
        private final ArrayList<LibroUserFragment> fragmentList = new ArrayList<>();
        private final ArrayList<String> fragmentTitleList = new ArrayList<>();
        private final Context context;

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, Context context) {
            super(fm, behavior);
            this.context = context;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        private void addFragmemt(LibroUserFragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }

    private void ValidacionUsuario() {//Correo Layut
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();

        } else {
            String email = firebaseUser.getEmail();
            binding.Subtitulo.setText(email);
        }
    }
}