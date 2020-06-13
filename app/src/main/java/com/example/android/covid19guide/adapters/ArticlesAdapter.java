package com.example.android.covid19guide.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.android.covid19guide.R;
import com.example.android.covid19guide.datamodels.Article;
import java.util.List;

/**
 * This Class provides the Adapter to populate items/cards inside of the RecyclerView.
 */
public class ArticlesAdapter extends RecyclerView.Adapter<ArticlesAdapter.ArticleViewHolder> {

    //Declaring the articlesList Object Instance
    private List<Article> mArticlesList;

    //Declaring an instance of the mContext
    private Context mContext;

    /**
     * This constructor is used to create an instance of the ArticlesAdapter using the articlesList as an input
     *
     * @param articlesList List<Article>: the List of Articles' objects which will be passed to the adapter
     * @param context      Context: is the context of the app
     */
    public ArticlesAdapter(Context context, List<Article> articlesList) {

        mContext = context;

        mArticlesList = articlesList;
    }

    /**
     * This method is called when RecyclerView needs a new RecyclerView.ViewHolder of the given type to represent an item.
     *
     * @param parent   ViewGroup: The ViewGroup into which the new View will be added after it is bound to an adapter position.
     * @param viewType int: The view type of the new View.
     * @return ArticleViewHolder: A new ViewHolder that holds a View of the given view type.
     */
    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent, false);

        return new ArticleViewHolder(itemView);
    }

    /**
     * This method is called by RecyclerView to display the data at the specified position.
     * This method should update the contents of the itemView to reflect the item at the given position.
     *
     * @param holder   ArticleViewHolder: The ViewHolder which should be updated to represent the contents of the item at the given position in the data set.
     * @param position int: The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {

        Article article = mArticlesList.get(position);

        String articleImageUrl = article.getArticleImageUrl();

        Glide.with(mContext).load(articleImageUrl).into(holder.articleImage);

        holder.title.setText(article.getTitle());

        holder.description.setText(article.getDescription());

        holder.publishedDate.setText(mContext.getString(R.string.article_date, article.getDate()));

        holder.section.setText(mContext.getString(R.string.article_section, article.getSection()));

        if (article.getAuthorName() != null) {

            holder.authorName.setText(mContext.getString(R.string.article_author_name, article.getAuthorName()));

        } else {
            holder.authorName.setVisibility(View.GONE);

            // create a Constraint Set
            ConstraintSet constraintSet = new ConstraintSet();

            // get constraints from layout
            constraintSet.clone(holder.layout);

            //Centers the widget holder.section horizontally to the left and right side on another widgets sides which is the constraint layout.
            constraintSet.centerHorizontally(holder.section.getId(), holder.layout.getId());

            // set new constraints
            constraintSet.applyTo(holder.layout);
        }
    }

    /**
     * This method returns the size of the List that contains the items we want to display.
     *
     * @return int: the number of articles in the mArticlesList
     */
    @Override
    public int getItemCount() {

        return mArticlesList.size();
    }

    /**
     * This method adds the data or the articlesList passed as an input as the data set to the adapter and then notify
     * the adapter
     **/
    public void addAll(List<Article> articlesList) {

        mArticlesList = articlesList;

        notifyDataSetChanged();
    }

    /**
     * This method clears the data set of the adapter and then notify the adapter
     **/
    public void clear() {

        mArticlesList.clear();

        notifyDataSetChanged();
    }

    /**
     * This class represents a ViewHolder called ArticleViewHolder that describes an item view and metadata about its place within the RecyclerView.
     */
    class ArticleViewHolder extends RecyclerView.ViewHolder {

        //Declaring all Object Variables
        ImageView articleImage;

        TextView title;

        TextView description;

        TextView section;

        TextView authorName;

        TextView publishedDate;

        Button readMoreButton;

        ConstraintLayout layout;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);

            //Initializing all object variables
            articleImage = itemView.findViewById(R.id.article_image);

            title = itemView.findViewById(R.id.article_title);

            description = itemView.findViewById(R.id.article_description);

            section = itemView.findViewById(R.id.article_section);

            authorName = itemView.findViewById(R.id.article_author);

            publishedDate = itemView.findViewById(R.id.article_date);

            layout = itemView.findViewById(R.id.constraint_layout);

            readMoreButton = itemView.findViewById(R.id.read_more_button);

            readMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // Get the Adapter position of the item represented by this ViewHolder
                    int position = getAdapterPosition();

                    // Find the current article that was clicked on
                    Article article = mArticlesList.get(position);

                    // Convert the String URL into a URI object (to pass into the Intent constructor)
                    Uri articleUri = Uri.parse(article.getUrl());

                    // Create a new intent to view the article URI
                    Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                    // Send the intent to launch a new activity
                    if (websiteIntent.resolveActivity(mContext.getPackageManager()) != null) {
                        mContext.startActivity(websiteIntent);
                    }
                }
            });
        }
    }
}
