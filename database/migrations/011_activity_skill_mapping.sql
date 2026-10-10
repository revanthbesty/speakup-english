create table if not exists activity_skills (
  activity_id uuid not null references activities(id) on delete cascade,
  skill text not null,
  primary key(activity_id,skill)
);
insert into activity_skills(activity_id,skill)
select a.id, case a.type when 'VERB_INTRODUCTION' then 'VOCABULARY' when 'SENTENCE_BUILDER' then 'SENTENCE_CONSTRUCTION' when 'SPEAKING' then 'SPEAKING' else 'GENERAL_ENGLISH' end
from activities a
on conflict do nothing;
