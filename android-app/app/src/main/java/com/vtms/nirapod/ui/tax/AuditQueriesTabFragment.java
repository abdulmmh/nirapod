package com.vtms.nirapod.ui.tax;

import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.*;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.*;
import com.vtms.nirapod.R;
import com.vtms.nirapod.data.api.RetrofitClient;
import com.vtms.nirapod.data.local.TokenManager;
import com.vtms.nirapod.data.model.AuditQuery;
import java.util.*;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class AuditQueriesTabFragment extends Fragment {

    private static final String ARG_CASE_ID = "case_id";
    private long caseId;
    private RecyclerView rv;
    private ProgressBar pb;
    private TextView tvEmpty;

    public static AuditQueriesTabFragment newInstance(long caseId) {
        AuditQueriesTabFragment f = new AuditQueriesTabFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_CASE_ID, caseId);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) caseId = getArguments().getLong(ARG_CASE_ID);
    }

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audit_tab_queries, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.rv_queries);
        pb = view.findViewById(R.id.pb_loading);
        tvEmpty = view.findViewById(R.id.tv_empty);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        loadQueries();
    }

    private void loadQueries() {
        TokenManager tm = new TokenManager(requireContext());
        pb.setVisibility(View.VISIBLE);
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<List<AuditQuery>> resp = RetrofitClient.auditApi(tm).getQueries(caseId).execute();
                requireActivity().runOnUiThread(() -> {
                    pb.setVisibility(View.GONE);
                    if (resp.isSuccessful() && resp.body() != null) {
                        List<AuditQuery> list = resp.body();
                        if (list.isEmpty()) tvEmpty.setVisibility(View.VISIBLE);
                        else setupList(list);
                    }
                });
            } catch (Exception ignored) {}
        });
    }

    private void setupList(List<AuditQuery> list) {
        rv.setAdapter(new RecyclerView.Adapter<QueryVH>() {
            @NonNull @Override public QueryVH onCreateViewHolder(@NonNull ViewGroup p, int vt) {
                View v = LayoutInflater.from(p.getContext()).inflate(R.layout.item_audit_query, p, false);
                return new QueryVH(v);
            }
            @Override public void onBindViewHolder(@NonNull QueryVH h, int p) {
                AuditQuery q = list.get(p);
                h.tvNo.setText(q.getQueryNo());
                h.tvType.setText(q.getQueryType());
                h.tvSubject.setText(q.getSubject());
                h.tvText.setText(q.getQueryText());
                h.tvDeadline.setText("Due: " + q.getDeadline());
                
                if (q.getResponseText() != null && !q.getResponseText().isEmpty()) {
                    h.layoutResponse.setVisibility(View.VISIBLE);
                    h.tvResponse.setText(q.getResponseText());
                    h.tvRespondedAt.setText("Responded: " + q.getRespondedAt());
                } else {
                    h.layoutResponse.setVisibility(View.GONE);
                }
            }
            @Override public int getItemCount() { return list.size(); }
        });
    }

    static class QueryVH extends RecyclerView.ViewHolder {
        TextView tvNo, tvType, tvDeadline, tvSubject, tvText, tvResponse, tvRespondedAt;
        View layoutResponse;
        QueryVH(View v) {
            super(v);
            tvNo = v.findViewById(R.id.tv_query_no);
            tvType = v.findViewById(R.id.tv_query_type);
            tvDeadline = v.findViewById(R.id.tv_deadline);
            tvSubject = v.findViewById(R.id.tv_subject);
            tvText = v.findViewById(R.id.tv_query_text);
            tvResponse = v.findViewById(R.id.tv_response_text);
            tvRespondedAt = v.findViewById(R.id.tv_responded_at);
            layoutResponse = v.findViewById(R.id.layout_response);
        }
    }
}
