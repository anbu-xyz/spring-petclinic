@import java.util.List

@param String label
@param String name
@param List<String> items
@param String selected
@param boolean hasErrors
@param String errors

<div class="form-group ${hasErrors ? "has-error" : ""}">
    <label class="col-sm-2 control-label">${label}</label>
    <div class="col-sm-10">
        <select name="${name}">
            @for(String item : items)
                <option value="${item}" ${item.equals(selected) ? 'selected' : ''}>${item}</option>
            @endfor
        </select>
        @if(!hasErrors)
            <span class="fa fa-ok form-control-feedback" aria-hidden="true"></span>
        @else
            <span class="fa fa-remove form-control-feedback" aria-hidden="true"></span>
            <span class="help-inline">${errors}</span>
        @endif
    </div>
</div>
