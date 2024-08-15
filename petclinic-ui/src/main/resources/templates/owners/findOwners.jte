@import uk.anbu.spring.sample.petclinic.dto.OwnerDto

@param OwnerDto owner

<h2>Find Owners</h2>

<form action="/owners" method="get" class="form-horizontal" id="search-owner-form">
  <div class="form-group">
    <div class="control-group" id="lastNameGroup">
      <label class="col-sm-2 control-label">Last name </label>
      <div class="col-sm-10">
        <input class="form-control" name="lastName" value="${owner.getLastName()}" size="30" maxlength="80" />
        @if(owner.hasErrors())
        <span class="help-inline">
            @for(String error : owner.getErrors())
              <p>${error}</p>
            @endfor
          </span>
        @endif
      </div>
    </div>
  </div>
  <div class="form-group">
    <div class="col-sm-offset-2 col-sm-10">
      <button type="submit" class="btn btn-primary">Find Owner</button>
    </div>
  </div>

  <a class="btn btn-primary" href="/owners/new">Add Owner</a>
</form>
