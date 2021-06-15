package adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.notetestserver.MainActivity;
import com.example.notetestserver.R;
import com.example.notetestserver.ShowNoteAvtivity;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import interfaces.NoteStateChangeListener;
import object.*;
import app.*;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {

    Activity activity;
    List<NoteObject> list;

    public NoteAdapter(Activity activity, List<NoteObject> list) {
        this.activity = activity;
        this.list = list;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_note_row, parent, false);
        return new NoteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(NoteAdapter.NoteViewHolder holder, int position) {
        holder.title.setText(list.get(position).getTitle());
        holder.message.setText(list.get(position).getMessage());
        holder.circleImageView.setImageDrawable(app.getImage(list.get(position)));
        holder.imageView.setImageResource(
                list.get(position).getSeen() == 1 ? R.drawable.ic_round_remove_red_eye_24 : R.drawable.ic_round_visibility_off_24
        );
        holder.title.setTextColor(spref.get().getInt("textcolor", Color.BLACK));
        holder.title.setTypeface(MainActivity.font);
        holder.message.setTypeface(MainActivity.font);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView title, message;
        ImageView circleImageView;
        ImageView imageView;


        public NoteViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            message = itemView.findViewById(R.id.message);
            circleImageView = itemView.findViewById(R.id.circleImageView1);
            imageView = itemView.findViewById(R.id.circleImageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ShowNoteAvtivity.class);
                    intent.putExtra("object", list.get(getAdapterPosition()));
                    app.pos = getAdapterPosition();
                    activity.startActivity(intent);
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    app.changeNoteState(0, list.get(getAdapterPosition()), new NoteStateChangeListener() {
                        @Override
                        public void onChange(int position, int noteId, int state, boolean sucses) {
                            NoteObject noteObject = list.get(getAdapterPosition());
                            if (sucses) {
                                noteObject.setSeen(state);
                                list.set(getAdapterPosition(), noteObject);
                                imageView.setImageResource(
                                        noteObject.getSeen() == 1 ? R.drawable.ic_round_remove_red_eye_24 : R.drawable.ic_round_visibility_off_24
                                );
                            }
                        }

                        @Override
                        public void onStart() {

                        }

                        @Override
                        public void onFinish() {

                        }

                    });
                }
            });
        }

    }
}
