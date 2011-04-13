function showLoading(target_div, loading_what) {
    loading_what2 = loading_what == "" ? loading_what : " " + loading_what;
    l_html = '<table><tr><td><p class="loading">Loading' + loading_what2 + '...</p></td></tr><tr><td style="text-align: center;"><img src="/resources/loading_anim.gif" width="50"></td></tr></table>';
    $('#' + target_div).append(l_html);
}

function hideLoading(target_div) {
    $('#' + target_div).empty();
}