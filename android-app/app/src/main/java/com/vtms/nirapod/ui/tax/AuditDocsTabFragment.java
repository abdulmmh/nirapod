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
import com.vtms.nirapod.data.model.AuditDocumentRequest;
import java.util.*;
import java.util.concurrent.Executors;
import retrofit2.Response;

public class AuditDocsTabFragment extends Fragment {

    private static final String ARG_CASE_ID = "case_id";
    private long caseId;
    private RecyclerView rv;
    private ProgressBar pb;
    private TextView tvEmpty;

    public static AuditDocsTabFragment newInstance(long caseId) {
        AuditDocsTabFragment f = new AuditDocsTabFragment();
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
        return inflater.inflate(R.layout.fragment_audit_tab_docs, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rv = view.findViewById(R.id.rv_docs);
        pb = view.findViewById(R.id.pb_loading);
        tvEmpty = view.findViewById(R.id.tv_empty);
        rv.setLayoutManager(new LinearLayoutManager(requireContext()));
        loadDocs();
    }

    private void loadDocs() {
        TokenManager tm = new TokenManager(requireContext());
        pb.setVisibility(View.VISIBLE);
        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                Response<List<AuditDocumentRequest>> resp = RetrofitClient.auditApi(tm).getDocumentRequests(caseId).execute();
                requireActivity().runOnUiThread(() -> {
                    pb.setVisibility(View.GONE);
                    if (resp.isSuccessful() && resp.body() != null) {
                        List<AuditDocumentRequest> list = resp.body();
                        if (list.isEmpty()) tvEmpty.setVisibility(View.VISIBLE);
                        else setupList(list);
                    }
                });
            } catch (Exception ignored) {}
        });
    }

    private void setupList(List<AuditDocumentRequest> list) {
        rv.setAdapter(new RecyclerView.Adapter<DocVH>() {
            @NonNull @Override public DocVH onCreateViewHolder(@NonNull ViewGroup p, int vt) {
                View v = LayoutInflater.from(p.getContext()).inflate(R.layout.item_audit_doc_request, p, false);
                return new DocVH(v);
            }
            @Override public void onBindViewHolder(@NonNull DocVH h, int p) {
                AuditDocumentRequest dr = list.get(p);
                h.tvNo.setText(dr.getRequestNo());
                h.tvType.setText(dr.getRequestType());
                h.tvStatus.setText(dr.getStatus());
                h.tvDeadline.setText("Due: " + dr.getDeadline());
                h.tvDocs.setText("📄 " + dr.getRequestedDocuments());
                h.tvReason.setText(dr.getRequestReason());
                
                if (dr.getFulfillmentNotes() != null && !dr.getFulfillmentNotes().isEmpty()) {
                    h.layoutFulfilled.setVisibility(View.VISIBLE);
                    h.tvNotes.setText("Fulfilled: " + dr.getFulfillmentNotes());
                } else {
                    h.layoutFulfilled.setVisibility(View.GONE);
                }
            }
            @Override public int getItemCount() { return list.size(); }
        });
    }

    static class DocVH extends RecyclerView.ViewHolder {
        TextView tvNo, tvType, tvStatus, tvDeadline, tvDocs, tvReason, tvNotes;
        View layoutFulfilled;
        DocVH(View v) {
            super(v);
            tvNo = v.findViewById(R.id.tv_request_no);
            tvType = v.findViewById(R.id.tv_request_type);
            tvStatus = v.findViewById(R.id.tv_doc_status);
            tvDeadline = v.findViewById(R.id.tv_doc_deadline);
            tvDocs = v.findViewById(R.id.tv_requested_docs);
            tvReason = v.findViewById(R.id.tv_request_reason);
            tvNotes = v.findViewById(R.id.tv_fulfillment_notes);
            layoutFulfilled = v.findViewById(R.id.layout_fulfilled);
        }
    }
}
