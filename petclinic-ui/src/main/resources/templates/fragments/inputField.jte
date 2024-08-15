@param String label
@param String name
@param String type
@param String value
@param boolean hasErrors
@param String errors

<div class="form-group ${hasErrors ? "has-error" : ""}">
    <label class="col-sm-2 control-label">${label}</label>
    <div class="col-sm-10">
        @if(type.equals("text"))
            <input class="form-control" type="text" name="${name}" value="${value}" />
        @elseif(type.equals("date"))
            <input class="form-control" type="date" name="${name}" value="${value}" />
        @endif
        @if(!hasErrors)
            <span class="fa fa-ok form-control-feedback" aria-hidden="true"></span>
        @else
            <span class="fa fa-remove form-control-feedback" aria-hidden="true"></span>
            <span class="help-inline">${errors}</span>
        @endif
    </div>
</div>
